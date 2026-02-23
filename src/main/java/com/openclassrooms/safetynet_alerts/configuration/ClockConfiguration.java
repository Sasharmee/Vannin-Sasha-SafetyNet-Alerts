package com.openclassrooms.safetynet_alerts.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Configuration permettant de fournir un bean Clock
 * Permet l'injection d'un Clock dans les composants qui en ont le besoin pour la réalisation d'opération basées sur le temps
 */

@Configuration
public class ClockConfiguration {

    /**
     * Fournit un Clock système par défaut
     * Ce bean peut être surchargé dans les tests pour utiliser un Clock fixe
     *
     * @return Clock configuré avec le fuseau horaire du système
     */

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
