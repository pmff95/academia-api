package com.example.demo.common.security.accesscontrol.policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccessPolicyFactory {

    private final Map<String, AccessPolicy> policyMap;

    @Autowired
    public AccessPolicyFactory(List<AccessPolicy> policies) {
        this.policyMap = policies.stream()
                .collect(Collectors.toMap(AccessPolicy::getEntityName, Function.identity()));
    }

    public AccessPolicy getPolicy(String entityName) {
        return Optional.ofNullable(policyMap.get(entityName))
                .orElseThrow(() -> new AccessDeniedException("Política de acesso não encontrada para a entidade: " + entityName));
    }

}
