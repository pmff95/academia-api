package com.example.demo.dto.professor;

import com.example.demo.common.validation.annotation.CPF;
import com.example.demo.dto.usuario.EnderecoRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import com.example.demo.domain.enums.Sexo;

public record ProfessorRequest(

        @Schema(description = "Nome completo do professor", example = "Maria Oliveira")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Nome completo do pai do professor", example = "José da Silva")
        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        String pai,

        @Schema(description = "Nome completo da mãe do professor", example = "Maria Oliveira")
        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        String mae,

        @Schema(description = "Email do professor", example = "professor@email.com")
        @Email(message = "Informe um email válido")
        String email,

        @Schema(description = "CPF do professor", example = "000.000.000-00")
        @NotBlank(message = "O CPF é obrigatório")
        @CPF(message = "CPF inválido")
        String cpf,

        @Schema(description = "Data de nascimento do professor", example = "1990-01-01")
        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate dataNascimento,

        @Schema(description = "Sexo do professor", example = "MASCULINO")
        Sexo sexo,

        @Schema(description = "Telefone do professor", example = "11999999999")
        String telefone,

        @Schema(description = "Número do cartão do professor (caso utilize sistema de entrada)", example = "1234567890")
        @Size(min = 1, max = 30, message = "Número do cartão deve ter entre 1 e 30 caracteres")
        String numeroCartao,

        @Schema(description = "UUIDs das disciplinas associadas ao professor", example = "[\"a23e4b56-789c-4efb-b012-123456789abc\"]")
        List<UUID> disciplinasIds,

        @Schema(description = "Horários disponíveis do professor")
        List<HorarioDisponivelRequest> horariosDisponiveis,

        @Schema(description = "Endereço do professor")
        EnderecoRequest endereco,

        @Schema(description = "Matrícula do professor")
        String matricula
) {
}
