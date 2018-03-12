package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.Domain;
import com.avantir.blowfish.services.DomainService;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lekanomotayo on 06/02/2018.
 */

@RestController
@RequestMapping(value = "api/v1/domains", produces = "application/hal+json")
public class DomainController {


    private static final Logger logger = LoggerFactory.getLogger(DomainController.class);

    @Autowired
    DomainService domainService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody Domain domain, HttpServletResponse response)
    {
        try{
            domainService.create(domain);
            domain = getLinks(domain, response);
            response.setStatus(HttpServletResponse.SC_CREATED);
            return domain;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody Domain domain, HttpServletResponse response)
    {
        try{
            if(domain == null)
                throw new Exception();

            domain.setDomainId(id);
            domain = domainService.update(domain);
            response.setStatus(HttpServletResponse.SC_OK);
            domain = getLinks(domain, response);
            return domain;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            domainService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="code", required = false) String code, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);

            if(code != null && !code.isEmpty())
                return getByCode(code, response);

            List<Domain> domainList = domainService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (Domain domain : domainList) {
                domain = getLinks(domain, response);
            }
            return domainList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable("id") long id, HttpServletResponse response)
    {
        try
        {
            Domain domain = domainService.findByDomainId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            domain = getLinks(domain, response);
            return domain;
        }
        catch(Exception ex)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    public Object getByCode(String code, HttpServletResponse response)
    {
        String fxnParams = "code=" + code + ",HttpServletResponse=" + response.toString();
        try
        {
            Domain domain = domainService.findByCode(code);
            response.setStatus(HttpServletResponse.SC_OK);
            domain = getLinks(domain, response);

            return domain;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    private Domain getLinks(Domain domain, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(DomainController.class).slash(domain.getDomainId()).withSelfRel();
        domain.add(selfLink);
        return domain;
    }
}