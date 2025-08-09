package com.example.demo.dto.usuario;

import com.example.demo.common.validation.annotation.CPF;
import com.example.demo.domain.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UsuarioRequest(

        @Schema(description = "UUID do usuário")
        UUID uuid,

        @Schema(description = "UUID da escola do usuário")
        UUID escolaId,

        @Schema(description = "Nome do usuário")
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
        String nome,

        @Schema(description = "Foto do usuário")
        MultipartFile foto,

        @Schema(description = "Email do usuário")
        @Email(message = "Informe um email válido")
        String email,

        @Schema(description = "Matrícula do usuário")
        String matricula,

        @Schema(description = "CPF do usuário")
        @CPF(message = "CPF inválido")
        String cpf,

        @Schema(description = "Telefone de contato")
        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @Schema(description = "Perfil de acesso")
        @NotNull(message = "O perfil é obrigatório")
        Perfil perfil

) {
    public UsuarioRequest withEscolaIdAndPerfil(UUID escolaId, Perfil perfil) {
        return new UsuarioRequest(uuid, escolaId, nome, foto, email, matricula, cpf, telefone, perfil);
    }
}
