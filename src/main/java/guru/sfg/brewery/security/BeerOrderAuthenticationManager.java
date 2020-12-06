package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.OrderStatusEnum;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BeerOrderAuthenticationManager {

    BeerOrderRepository beerOrderRepository;

    public boolean customerIdMatches(Authentication authentication, UUID customerId) {
        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Auth User Customer id: " + authenticatedUser.getCustomer().getId() + " Customer Id: " + customerId);

        return authenticatedUser.getCustomer().getId().equals(customerId);
    }

    public boolean beerOrderReady(UUID orderId) {
        return beerOrderRepository.findOneById(orderId).getOrderStatus().equals(OrderStatusEnum.READY);
    }
}
