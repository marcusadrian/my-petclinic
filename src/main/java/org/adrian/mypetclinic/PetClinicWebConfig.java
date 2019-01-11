package org.adrian.mypetclinic;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * During front development, a nodejs server runs to serve the Angular SPA.
 * So we need to enable cross origin.
 * In production this won't be the case as the Angular SPA is served from the
 * distributed jar with no cross origin involved.
 */
@Configuration
@Profile(PetclinicApplication.NOT_DEV_PROFILE)
public class PetClinicWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        String webappFolder = "webapp";
        String indexForward = String.format("forward:/%s/index.html", webappFolder);
        String indexRedirect = String.format("redirect:/%s/index.html", webappFolder);

        // redirect to index.html for request of the context root
        registry.addViewController("/").setViewName(indexRedirect);
        registry.addViewController("/index.html").setViewName(indexRedirect);
        // forward to index.html for request of the webapp root
        registry.addViewController(String.format("/%s", webappFolder)).setViewName(indexForward);
        registry.addViewController(String.format("/%s/", webappFolder)).setViewName(indexForward);

        // support correct forward whenever F5 is pressed
        // all base routes must be set here to serve index.html in case user presses F5
        String[] angularRoutes = {"home", "owners"};

        for (String angularRoute : angularRoutes) {
            registry.addViewController(String.format("/%s/%s/**", webappFolder, angularRoute))
                    .setViewName(indexForward);
        }
    }
}