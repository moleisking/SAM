import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizationService } from "@angular/platform-browser";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";

import { MessageModel } from "../models/message";

declare var jQuery: any;

@Component({
    selector: "profile-component",
    templateUrl: "../../views/profile.html"
})

export class Profile implements OnInit, OnDestroy {

    private sub: any;
    private message: string;
    private description: string;
    private mobile: string;
    private address: string;
    private image: string;
    private name: string;
    private email: string;

    constructor(private route: ActivatedRoute, private user: UserService, private sanitizer: DomSanitizationService,
        private m: MessageService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            this.user.getProfile(id).subscribe(
                profile => {
                    this.name = profile.name;
                    this.email = profile.email;
                    this.description = profile.description;
                    this.mobile = profile.mobile;
                    this.address = profile.address;
                    this.image = profile.image;
                },
                error => this.message = <any>error
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    sendMessage(messageText: any) {
        if (!messageText || !this.email || this.email === "undefined")
            this.message = "Message not valid to be sent.";
        else if (messageText.length < 4 || messageText.length > 500)
            this.message = "Message not cannot be less that 3 characters or bigger than 500.";
        else {
            this.message = "Message profile sent...";

            let model = new MessageModel();
            model.to = this.email;
            model.text = messageText;

            this.m.add(model).subscribe(
                () => this.message = "Message sent.",
                (res) => this.message = res,
                () => jQuery("#SendMessageModal").modal("hide")
            );
        }
    }
}
