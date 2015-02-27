package provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;


/**
 * Only different from Jackson one is *+json in @Produces/@Consumes
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Provider
@Consumes({"application/*+yaml", "text/yaml", "text/x-yaml"})
@Produces({"application/*+yaml", "text/yaml", "text/x-yaml"})
public class JacksonJaxbYamlProvider extends JacksonJaxbJsonProvider
{
	
    /**
     * Default constructor, usually used when provider is automatically
     * configured to be used with JAX-RS implementation.
     */
    public JacksonJaxbYamlProvider() {
        super();
    }

    /**
     * Helper method used to check whether given media type
     * is supported by this provider.
     * Current implementation essentially checks to see whether
     * {@link MediaType#getSubtype} returns "json" or something
     * ending with "+json".
     * Or "text/x-json" (since 2.3)
     * 
     * @since 2.2
     */
    @Override
    protected boolean hasMatchingMediaType(MediaType mediaType)
    {
        /* As suggested by Stephen D, there are 2 ways to check: either
         * being as inclusive as possible (if subtype is "json"), or
         * exclusive (major type "application", minor type "json").
         * Let's start with inclusive one, hard to know which major
         * types we should cover aside from "application".
         */
        if (mediaType != null) {
            // Ok: there are also "xxx+json" subtypes, which count as well
            String subtype = mediaType.getSubtype();

            // [Issue#14]: also allow 'application/javascript'
            return "yaml".equalsIgnoreCase(subtype) || subtype.endsWith("+yaml")
                   || "x-yaml".equals(subtype);
        }
        /* Not sure if this can happen; but it seems reasonable
         * that we can at least produce JSON without media type?
         */
        return true;
    }

   /**
    * Method called to locate {@link ObjectMapper} to use for serialization
    * and deserialization. If an instance has been explicitly defined by
    * {@link #setMapper} (or non-null instance passed in constructor), that
    * will be used. 
    * If not, will try to locate it using standard JAX-RS
    * {@link ContextResolver} mechanism, if it has been properly configured
    * to access it (by JAX-RS runtime).
    * Finally, if no mapper is found, will return a default unconfigured
    * {@link ObjectMapper} instance (one constructed with default constructor
    * and not modified in any way)
    *
    * @param type Class of object being serialized or deserialized;
    *   not checked at this point, since it is assumed that unprocessable
    *   classes have been already weeded out,
    *   but will be passed to {@link ContextResolver} as is.
    * @param mediaType Declared media type for the instance to process:
    *   not used by this method,
    *   but will be passed to {@link ContextResolver} as is.
    */
    @Override
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType)
    {       
		 ObjectMapper m = _mapperConfig.getConfiguredMapper();
		 if (m == null) {
			 // If not, maybe we can get one configured via context?
			 m = _locateMapperViaProvider(type, mediaType);
			 if (m == null) {
				 // If not, let's get the fallback default instance
				 m = new YAMLMapper();
				 try {
					m.setAnnotationIntrospector(JaxbAnnotationIntrospector.class.newInstance());
				 } catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 } catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				 }
			 }
		 }

		 return m;
   	}
   
}