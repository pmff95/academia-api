package com.example.dto.cobv;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ConfigurarWebhookRequest(
    String webhookUrl
) {}