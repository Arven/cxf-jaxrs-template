/**
 * These are a set of example services, implemented for the purpose of showing
 * how to use JAX-RS in a standard way. The configuration is set up to use CXF
 * as the JAX-RS implementation, Spring as the DI implementation, as well as
 * Spring Security for web security and authentication. This is intended to
 * keep this template, as well as future software designed from the template,
 * relatively agnostic to the framework that is being used and the container.
 * 
 * @author Brian Becker
 */
@XmlSchema(namespace = "http://github.com/Arven/spring-cxf-example", elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = @XmlNs(namespaceURI = "http://github.com/Arven/spring-cxf-example", prefix = "api"))
package com.github.arven.rs.services.example;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;