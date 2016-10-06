import { Component, OnInit } from "@angular/core";
import { WebService } from "../services/web";

@Component({
    selector: "cookiepolicy-component",
    templateUrl: "../../views/termsconditions.html"
})

export class CookiePolicy implements OnInit {

    private message: string;

    constructor(private web: WebService) {
        this.message = "Here will come Cookie Policy Text from backend.";
    }

    getCookiePolicyText() {
        this.web.cookiePolicy().subscribe(
            text => this.message = text,
            error => this.message = <any>error
        );
    }

    ngOnInit() {
        this.getCookiePolicyText();
    }
}
