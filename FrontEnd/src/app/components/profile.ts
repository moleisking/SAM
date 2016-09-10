import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizationService } from "@angular/platform-browser";

import { UserService } from "../services/user";

@Component({
    selector: "profile-component",
    templateUrl: "../../views/profile.html",
    providers: [UserService]
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    private message: string;
    private description: string;
    private mobile: string;
    private address: string;
    private image: string;
    private name: string;

    constructor(private route: ActivatedRoute, private user: UserService, private sanitizer: DomSanitizationService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            this.user.getProfile(id).subscribe(
                profile => {
                    this.name = profile.name;
                    this.description = profile.description;
                    this.mobile = profile.mobile;
                    this.address = profile.address;
                    this.image = profile.image;
                },
                error => this.message = <any>error);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}