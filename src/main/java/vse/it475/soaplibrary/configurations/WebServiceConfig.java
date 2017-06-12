package vse.it475.soaplibrary.configurations;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Created by hofmanix on 29/04/2017.
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    /**
     * Bean for mapping calling actions
     * @param applicationContext
     * @return
     */
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(false);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    /**
     * Sets required informations for generation of wsdl file
     * @param librarySchema
     * @return
     */
    @Bean(name = "library")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema librarySchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("LibraryPort");
        wsdl11Definition.setLocationUri("http://library.hofmanix.cz/ws");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(librarySchema);
        return wsdl11Definition;
    }

    /**
     * Schemes path
     * @return
     */
    @Bean
    public XsdSchema librarySchema() {
        return new SimpleXsdSchema(new ClassPathResource("library.xsd"));
    }
}
