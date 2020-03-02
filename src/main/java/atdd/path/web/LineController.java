package atdd.path.web;

import atdd.path.application.dto.LineRequestView;
import atdd.path.application.dto.LineResponseView;
import atdd.path.service.EdgeService;
import atdd.path.service.LineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;
    private EdgeService edgeService;

    public LineController(LineService lineService, EdgeService edgeService) {
        this.lineService = lineService;
        this.edgeService = edgeService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody LineRequestView requestView){
        LineResponseView responseView = lineService.create(requestView);
        return ResponseEntity
                .created(URI.create("/lines/"+responseView.getId()))
                .body(responseView);
    }
}
