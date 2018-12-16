package org.adrian.mypetclinic;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * During front development, a nodejs server runs to serve the Angular SPA.
 * So we need to enable cross origin.
 * In production this won't be the case as the Angular SPA is served from the
 * distributed jar with no cross origin involved.
 */
@Configuration
@Profile(PetclinicApplication.DEV_PROFILE)
public class PetClinicDevWebConfig extends PetClinicWebConfig {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

}