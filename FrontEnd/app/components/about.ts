import { Component, OnInit } from "@angular/core";
import { WebService } from "../services/web";

@Component({
    selector: "about-component",
    templateUrl: "/views/about.html",
    providers: [WebService]
})

export class About implements OnInit {
    message: string;

    constructor(private web: WebService) {
        this.message = "About Text";
    }

    getAboutText() {
        this.web.about().then(
            (res) => {
                this.message = res.toString();
            }
        )
    }

    ngOnInit() {
        this.getAboutText();
    }
}