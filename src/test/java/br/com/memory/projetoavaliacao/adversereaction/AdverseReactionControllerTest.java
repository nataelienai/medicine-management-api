package br.com.memory.projetoavaliacao.adversereaction;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AdverseReactionController.class)
public class AdverseReactionControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AdverseReactionService adverseReactionService;

  @Test
  @DisplayName("GET /adverse-reactions should return 200 and paged list of adverse reactions when given filter and pagination")
  void getAdverseReactionsShouldReturn200AndPagedListOfAdverseReactionsWhenGivenFilterAndPagination() throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<AdverseReaction> adverseReactions = List.of(
        new AdverseReaction(1L, "Strong reaction"));
    Page<AdverseReaction> pagedAdverseReactions = new PageImpl<>(
        adverseReactions,
        pageable,
        adverseReactions.size());

    when(adverseReactionService.findAllBy("strong", pageable))
        .thenReturn(pagedAdverseReactions);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedAdverseReactions);
    mockMvc.perform(get("/adverse-reactions")
        .param("description", "strong")
        .param("page", "0")
        .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }
}