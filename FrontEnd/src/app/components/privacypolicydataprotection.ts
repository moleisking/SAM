import { Component, OnInit } from "@angular/core";
import { WebService } from "../services/web";

@Component({
    selector: "privacypolicydataprotection-component",
    templateUrl: "../../views/privacypolicydataprotection.html"
})

export class PrivacyPolicyDataProtectionComponent implements OnInit {

    private message: string;

    constructor(private web: WebService) {
        this.message = ""; // Here will come Privacy Policy Data Protection Text from backend.
    }

    getPrivacyPolicyDataProtectionText() {
        this.web.privacyPolicyDataProtection().subscribe(
            text => this.message = text,
            error => this.message = <any>error
        );
    }

    ngOnInit() {
        this.getPrivacyPolicyDataProtectionText();
    }
}
