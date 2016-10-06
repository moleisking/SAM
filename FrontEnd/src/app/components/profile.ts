import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizationService } from "@angular/platform-browser";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { AuthService } from "../services/auth";

import { ProfileModel } from "../models/profile";
import { MessageModel } from "../models/message";
import { UserDefaultImage } from "../config/userdefaultimage";

declare var jQuery: any;

@Component({
    selector: "profile-component",
    templateUrl: "../../views/profile.html"
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    private defaultImage = UserDefaultImage.image;
    private itsMe: boolean;

    private message: string;

    private model: ProfileModel = <ProfileModel>{};

    constructor(
        private route: ActivatedRoute,
        private user: UserService,
        private sanitizer: DomSanitizationService,
        private m: MessageService,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            let id = p["id"];
            this.user.getProfile(id).subscribe(
                profile => {
                    this.model = profile;
                    this.model.image = profile.image === "" ? this.defaultImage : profile.image;
                    if (this.authService.isLoggedIn()) {
                        this.user.getMyProfile().subscribe(
                            my => this.itsMe = profile.email === my.email,
                            error => this.message = <any>error,
                            () => console.log("Done get my profile")
                        );
                    }
                    else
                        this.itsMe = true; // for unlogged we use same logic as it were us.
                },
                error => this.message = <any>error,
                () => console.log("Done get profile")
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    sendMessage(messageText: string) {
        if (!messageText || !this.model.email || this.model.email === "undefined")
            this.message = "Message not valid to be sent.";
        else if (messageText.length < 4 || messageText.length > 500)
            this.message = "Message not cannot be less that 3 characters or bigger than 500.";
        else {
            this.message = "Message profile sent...";

            let model = new MessageModel();
            model.to = this.model.email;
            model.nameurl = this.model.nameurl;
            model.text = messageText;

            this.m.add(model).subscribe(
                () => this.message = "Message sent.",
                (res) => this.message = res,
                () => jQuery("#SendMessageModal").modal("hide")
            );
        }
    }
}
