package com.luminn.firebase.controller.quiz;


import com.luminn.firebase.dto.ShortRide;
import com.luminn.firebase.entity.Quiz;
import com.luminn.firebase.repository.QuizRepository;
import com.luminn.view.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    QuizRepository quizRepository;

    @ResponseBody
    @RequestMapping(value = "/app/kb" + "/{category}" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<StatusResponse> get(@PathVariable("category") String category)  {
        StatusResponse sr = new StatusResponse();
        sr.setData(quizRepository.findByCategory(category));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/app/add/kb" + "/{category}" , method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<StatusResponse> add(@RequestBody Quiz rideDTO)  {
        StatusResponse sr = new StatusResponse();
        sr.setData(quizRepository.save(rideDTO));
        return new ResponseEntity<>(sr, HttpStatus.OK);
    }
}
