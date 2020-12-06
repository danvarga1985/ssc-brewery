package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('beerOrder.read') OR " +
        "hasAuthority('customer.beerOrder.read') AND " +
        "@beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
public @interface BeerOrderReadPermission {
}
