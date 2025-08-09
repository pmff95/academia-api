package com.example.demo.dto.aluno;

import com.example.demo.common.validation.annotation.CPF;
import com.example.demo.domain.enums.GrupoSanguineo;
import com.example.demo.domain.enums.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;


public record AlunoRequest(
        @Schema(description = "Nome completo do aluno", example = "João da Silva")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Email do aluno", example = "email@email.com")
        @Email(message = "Informe um email válido")
        String email,

        @Schema(description = "Sexo do aluno", example = "Masculino / Feminino")
        @NotNull(message = "O sexo é obrigatório")
        Sexo sexo,

        @Schema(description = "CPF do aluno", example = "000.000.000-00")
        @CPF(message = "CPF inválido")
        String cpf,

        @Schema(description = "Data de nascimento do aluno", example = "1990-01-01")
        @NotNull(message = "Data de nascimento é obrigatória")
        LocalDate dataNascimento,

        @Schema(description = "Telefone do aluno", example = "00000000000")
        String telefone,

        @Schema(description = "Grupo sangu\u00edneo do aluno", example = "O_POSITIVO")
        GrupoSanguineo grupoSanguineo,

        @Schema(description = "Número de matrícula do aluno", example = "00000000")
        String matricula,

        @Schema(description = "Número do cartão", example = "00000000000")
        @Size(min = 1, max = 30, message = "Número deve ter entre 1 e 30 caracteres")
        String numeroCartao,

        @Schema(description = "Foto do aluno")
        MultipartFile foto,

        @Schema(description = "Uuid da turma")
        UUID turmaUuid,

        @Schema(description = "Uuid do itinerário formativo")
        UUID itinerarioUuid,

        @Schema(description = "Dados de responsáveis do aluno")
        ResponsavelAlunoRequest responsavelAlunoRequest,

        @Schema(description = "Dados de responsáveis do aluno")
        List<AlergiaRequest> alergias,

        @Schema(description = "Medicamentos utilizados pelo aluno")
        List<MedicamentoRequest> medicamentos

) {
}
