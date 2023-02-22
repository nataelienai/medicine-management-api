package br.com.memory.projetoavaliacao.adversereaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.memory.projetoavaliacao.shared.exception.ErrorResponse;

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

  @Test
  @DisplayName("GET /adverse-reactions should return 200 and paged list of adverse reactions when not given filter and pagination")
  void getAdverseReactionsShouldReturn200AndPagedListOfAdverseReactionsWhenNotGivenFilterAndPagination()
      throws Exception {
    // given
    Pageable pageable = PageRequest.of(0, 10);
    List<AdverseReaction> adverseReactions = List.of(
        new AdverseReaction(1L, "Strong reaction"));
    Page<AdverseReaction> pagedAdverseReactions = new PageImpl<>(
        adverseReactions,
        pageable,
        adverseReactions.size());

    when(adverseReactionService.findAllBy(eq(null), any(Pageable.class)))
        .thenReturn(pagedAdverseReactions);

    // when
    // then
    String expected = objectMapper.writeValueAsString(pagedAdverseReactions);
    mockMvc.perform(get("/adverse-reactions"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("POST /adverse-reactions should return 201 and adverse reaction when given valid input")
  void postAdverseReactionsShouldReturn201AndAdverseReactionWhenGivenValidInput() throws Exception {
    // given
    AdverseReaction adverseReaction = new AdverseReaction(1L, "Strong reaction");
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto(
        adverseReaction.getDescription());

    when(adverseReactionService.create(adverseReactionDto))
        .thenReturn(adverseReaction);

    String serializedAdverseReactionDto = objectMapper.writeValueAsString(adverseReactionDto);

    // when
    // then
    String expected = objectMapper.writeValueAsString(adverseReaction);
    mockMvc.perform(post("/adverse-reactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedAdverseReactionDto))
        .andExpect(status().isCreated())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("POST /adverse-reactions should return 400 when given a null description")
  void postAdverseReactionsShouldReturn400WhenGivenNullDescription() throws Exception {
    // given
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto(null);
    String serializedAdverseReactionDto = objectMapper.writeValueAsString(adverseReactionDto);

    // when
    // then
    ErrorResponse errorResponse = new ErrorResponse(400, "The description field must not be blank");
    String expected = objectMapper.writeValueAsString(errorResponse);
    mockMvc.perform(post("/adverse-reactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedAdverseReactionDto))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("POST /adverse-reactions should return 400 when given an empty description")
  void postAdverseReactionsShouldReturn400WhenGivenEmptyDescription() throws Exception {
    // given
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto("");
    String serializedAdverseReactionDto = objectMapper.writeValueAsString(adverseReactionDto);

    // when
    // then
    ErrorResponse errorResponse = new ErrorResponse(400, "The description field must not be blank");
    String expected = objectMapper.writeValueAsString(errorResponse);
    mockMvc.perform(post("/adverse-reactions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedAdverseReactionDto))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("PUT /adverse-reactions/{id} should return 200 and updated adverse reaction when given valid input")
  void putAdverseReactionsShouldReturn200AndUpdatedAdverseReactionWhenGivenValidInput() throws Exception {
    // given
    AdverseReaction adverseReaction = new AdverseReaction(1L, "Updated reaction");
    AdverseReactionDto adverseReactionDto = new AdverseReactionDto(
        adverseReaction.getDescription());

    when(adverseReactionService.update(adverseReaction.getId(), adverseReactionDto))
        .thenReturn(adverseReaction);

    String serializedAdverseReactionDto = objectMapper.writeValueAsString(adverseReactionDto);

    // when
    // then
    String expected = objectMapper.writeValueAsString(adverseReaction);
    mockMvc.perform(put("/adverse-reactions/{id}", adverseReaction.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(serializedAdverseReactionDto))
        .andExpect(status().isOk())
        .andExpect(content().string(expected));
  }

}
