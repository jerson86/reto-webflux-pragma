package com.pragma.powerup.domain.model;

import java.util.List;

public record Capability(Long id, String name, List<Technology> technologies) {}
