package com.example.demo.controller.dashboard;

import com.example.demo.common.doc.EurekaApiOperation;
import com.example.demo.common.response.ApiReturn;
import com.example.demo.dto.dashboard.DashboardUsuariosResponse;
import com.example.demo.dto.projection.dashboard.AniversarianteView;
import com.example.demo.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoints de dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/aniversariantes")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO','PROFESSOR')")
    @EurekaApiOperation(
            summary = "Listar aniversariantes do mês",
            description = "Retorna os aniversariantes de acordo com o mês informado."
    )
    public ResponseEntity<ApiReturn<List<AniversarianteView>>> aniversariantes(
            @RequestParam("mes") int mes
    ) {
        return ResponseEntity.ok(ApiReturn.of(dashboardService.buscarAniversariantesDoMes(mes)));
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasAnyRole('MASTER','ADMIN','FUNCIONARIO')")
    @EurekaApiOperation(
            summary = "Resumo de usuários",
            description = "Retorna o total de usuários da escola e quantidade por sexo separados por alunos e professores."
    )
    public ResponseEntity<ApiReturn<DashboardUsuariosResponse>> usuarios() {
        return ResponseEntity.ok(ApiReturn.of(dashboardService.obterResumoUsuarios()));
    }
}
