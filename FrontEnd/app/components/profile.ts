import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { UserService } from "../services/user";

@Component({
    selector: "profile-component",
    templateUrl: "/views/profile.html",
    providers: [UserService]
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    public message: string;
    public description: string;

    constructor(private route: ActivatedRoute, private user: UserService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            this.user.getProfile(id).subscribe(
                profile => this.description = profile.description,
                error => this.message = <any>error);
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}