package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

//    @Autowired
//    WebApplicationContext wac;
//
//    private MockMvc mockMvc;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;


    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Delete Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerBadCreds() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                    .header("Api-Key", "spring").header("Api-Secret", "guruXXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerBadCredsUrl() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                    .param("apiKey", "spring").header("apiSecret", "guruXXXX"))
                    .andExpect(status().isUnauthorized());
        }
    }

//    @Test
//    void findBeers() throws Exception {
//        mockMvc.perform(get("/api/v1/beer/"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void findBeerById() throws Exception {
//        Beer beer = beerRepository.findAll().get(0);
//
//        mockMvc.perform(get("/api/v1/beer/" + beer.getId()))
//                .andExpect(status().isOk());
//    }

    @Nested
    @DisplayName("Beer Upc Tests")
    class TestFindBeerByUpc {

        public Beer beerToFind() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Find Me Beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build());
        }

        @Test
        void findBeerByUpcUnauthenticated() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void findBeerByUpcWithAdmin() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc())
                    .with(httpBasic("spring", "guru")))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerByUpcWithUser() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc())
                    .with(httpBasic("user", "password")))
                    .andExpect(status().isOk());
        }

        @Test
        void findBeerByUpcWithCustomer() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + beerToFind().getUpc())
                    .with(httpBasic("scott", "tiger")))
                    .andExpect(status().isOk());
        }
    }


    @Test
    void findBeerFormAdmin() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("spring", "guru")))
                .andExpect(status().isOk());
    }
//    @Test
//    void deleteBeerUrl() throws Exception{
//        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
//                .param("apiKey","spring").param("apiSecret", "guru"))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void deleteBeer() throws Exception {
//        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
//                .header("Api-Key", "spring")
//                .header("Api-Secret", "guru"))
//                .andExpect(status().isOk());
//    }

}
