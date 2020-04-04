/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 *
 * @author brend
 */
    
public class PrettyMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
 /**
 * Construct a new {@link MappingJackson2HttpMessageConverter} using default configuration
 * provided by {@link Jackson2ObjectMapperBuilder}
 */
 public PrettyMappingJackson2HttpMessageConverter() {
 super();
 objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
 setSupportedMediaTypes(Lists.newArrayList(new MediaType("application", "json+pretty", DEFAULT_CHARSET)));
 }
}

