import { Component, OnInit } from "@angular/core";

import { WebService } from "../services/web";

@Component({
    selector: "termsconditions-component",
    templateUrl: "../../views/termsconditions.html"
})

export class TermsConditions implements OnInit {

    private message: string;

    constructor(
        private web: WebService
    ) {
        this.message = ""; // Here will come Terms & Conditions Text from backend.
    }

    getTermsConditionsText() {
        this.web.termsConditions().subscribe(
            text => this.message = text,
            error => this.message = <any>error
        );
    }

    ngOnInit() {
        this.getTermsConditionsText();
    }
}
