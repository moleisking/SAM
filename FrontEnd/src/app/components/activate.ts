import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { UserService } from "../services/user";
import { TranslateService } from "ng2-translate";

@Component({
    selector: "activate-component",
    templateUrl: "../../views/activate.html"
})

export class ActivateComponent implements OnInit, OnDestroy {

    private sub: any;

    private guid: string;
    private message: string;

    constructor(
        private route: ActivatedRoute,
        private user: UserService,
        private trans: TranslateService
    ) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            this.guid = p["id"];
            if (this.guid.length !== 32)
                this.trans.get("ActivationCodeNotValid").subscribe((res: string) => this.message = res);
            else {
                this.trans.get("ActivationCodeTrying").subscribe((res: string) => this.message = res);
                this.user.activate(this.guid).subscribe(
                    () => this.trans.get("ActivationCodeValid").subscribe((res: string) => this.message = res),
                    error => this.message = <any>error,
                    () => this.trans.get("DoneActivationCode").subscribe((res: string) => console.log(res))
                );
            }
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}
