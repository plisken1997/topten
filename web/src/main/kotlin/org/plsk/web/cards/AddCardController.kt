package org.plsk.web.cards

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class AddCardController {

    @GetMapping("/")
    fun blog(model: Model): String {
        model["title"] = "topten ftw"
        return "cards"
    }

}

@RestController
class RestCtl {

    @GetMapping("lol")
    fun lol() = "lol"
}