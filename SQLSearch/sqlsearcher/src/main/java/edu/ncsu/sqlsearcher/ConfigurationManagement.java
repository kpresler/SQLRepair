package edu.ncsu.sqlsearcher;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to tell Spring to use HTTPS and redirect all HTTP
 * requests to the secure version of this application. From
 * https://drissamri.be/blog/java/enable-https-in-spring-boot/
 *
 * @author Driss Amri
 *
 */
@Configuration
public class ConfigurationManagement {

    @Bean
    public TomcatServletWebServerFactory servletContainer () {
        final TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext ( final Context context ) {
                final SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint( "CONFIDENTIAL" );
                final SecurityCollection collection = new SecurityCollection();
                collection.addPattern( "/*" );
                securityConstraint.addCollection( collection );
                context.addConstraint( securityConstraint );
            }
        };

        tomcat.addAdditionalTomcatConnectors( initiateHttpConnector() );
        return tomcat;
    }

    private Connector initiateHttpConnector () {
        final Connector connector = new Connector( "org.apache.coyote.http11.Http11NioProtocol" );
        connector.setScheme( "http" );
        connector.setPort( 8080 );
        connector.setSecure( false );
        connector.setRedirectPort( 8443 );

        return connector;
    }
}
