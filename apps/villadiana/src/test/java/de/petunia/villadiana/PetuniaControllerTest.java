package de.petunia.villadiana;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetuniaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testGetPetuniaSpecies() throws Exception {
        mockMvc.perform(get("/api/petunias"))
                .andExpectAll(
                        (r) -> status().isOk(),
                        (r) -> content().string(containsString("Petunia"))
                );
    }
}
