package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {
    private String port;
    private String memory;
    private String index;
    private String addr;


    public EnvController(@Value("${port:8080}") String port,
                         @Value("${memory.limit:12G}") String memory,
                         @Value("${cf.instance.index:NOT SET}") String index,
                         @Value("${cf.instance.addr:1st street}") String addr){
        this.port = port;
        this.memory = memory;
        this.index = index;
        this.addr = addr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){
        Map<String, String> env = new HashMap<>();
        env.put("PORT", port);
        env.put("MEMORY_LIMIT", memory);
        env.put("CF_INSTANCE_INDEX", index);
        env.put("CF_INSTANCE_ADDR", addr);
        return env;
    }

}
