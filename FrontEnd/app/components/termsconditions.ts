import { Component, OnInit } from "@angular/core";
import { WebService } from "../services/web";

@Component({
    selector: "termsconditions-component",
    templateUrl: "/views/termsconditions.html",
    providers: [WebService]
})

export class TermsConditions implements OnInit {
    message: string;

    constructor(private web: WebService) {
        this.message = "Terms & Conditions Text";
    }

    getTermsConditionsText() {
        this.web.termsConditions().then(
            (res) => {
                this.message = res.toString();
            }
        )
    }

    ngOnInit() {
        this.getTermsConditionsText();
    }
}