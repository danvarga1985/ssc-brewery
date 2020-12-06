package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('beerOrder.create') OR " +
        "hasAuthority('customer.beerOrder.create') AND " +
        "@beerOrderAuthenticationManager.customerIdMatches(authentication, #customerId)")
public @interface BeerOrderCreatePermission {
}
