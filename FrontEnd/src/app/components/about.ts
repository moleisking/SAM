import { Component, OnInit } from "@angular/core";
import { WebService } from "../services/web";

@Component({
    selector: "about-component",
    templateUrl: "../../views/about.html",
})

export class About implements OnInit {

    private message: string;

    constructor(private web: WebService) {
        this.message = "Here will come About Text from backend.";
    }

    getAboutText() {
        this.web.about().subscribe(
            text => this.message = text,
            error => this.message = <any>error
        );
    }

    ngOnInit() {
        this.getAboutText();
    }
}
