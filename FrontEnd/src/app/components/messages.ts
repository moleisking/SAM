import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizationService } from "@angular/platform-browser";
import { Observable, Subscription } from "rxjs/Rx";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { AuthService } from "../services/auth";

import { MessageModel } from "../models/message";
import { ProfileModel } from "../models/profile";
import { UserDefaultImage } from "../config/userdefaultimage";

declare var jQuery: any;

@Component({
    selector: "messages-component",
    templateUrl: "../../views/messages.html"
})

export class Messages implements OnInit, OnDestroy {

    @ViewChild("messagetext") messageText: ElementRef;
    private sub: Subscription;
    private getProfile$: Subscription;
    private messages$: Subscription;

    private defaultImage = UserDefaultImage.image;
    private itsMe: boolean;

    private message: string;
    private image: string;
    private name: string;
    private nameurl: string;
    private email: string;
    private messages: MessageModel[];

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
            this.getProfile$ = this.user.getProfile(id).cache().subscribe(
                profile => {
                    this.name = profile.name;
                    this.nameurl = profile.nameurl;
                    this.email = profile.email;
                    this.image = profile.image === "" ? this.defaultImage : profile.image;
                    this.message = "Loading your messages...";
                    // setInterval(() => {
                        // if (location.pathname.indexOf("/messages/") !== -1)
                            this.messages$ = this.m.readWith(profile.nameurl).subscribe(
                                messages => {
                                    messages.forEach(element => {
                                        if (element.from === profile.email)
                                            element.from = profile.name;
                                        else
                                            element.from = "Me";
                                    });
                                    this.messages = messages;
                                    this.message = "";
                                },
                                error => this.message = <any>error,
                                () => console.log("Done get messages")
                            );
                    // }, 5000);
                },
                error => this.message = <any>error,
                () => console.log("Done get profile")
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
        this.getProfile$.unsubscribe();
        this.messages$.unsubscribe();
        console.log("all gone")
    }

    sendMessage(messageText: string) {
        if (!messageText || !this.email || this.email === "undefined" || !this.nameurl || this.nameurl === "undefined")
            this.message = "Message not valid to be sent.";
        else if (messageText.length < 4 || messageText.length > 500)
            this.message = "Message not cannot be less that 3 characters or bigger than 500.";
        else {
            this.message = "Message profile sent...";

            let model = new MessageModel();
            model.to = this.email;
            model.nameurl = this.nameurl;
            model.text = messageText;

            this.m.add(model).subscribe(
                (m) => {
                    this.message = "Message sent.";
                    m.from = "Me";
                    this.messages.push(m);
                    // clean the new message form
                    this.message = "";
                    this.messageText.nativeElement.value = "";
                },
                (res) => this.message = res,
                () => jQuery("#SendMessageModal").modal("hide")
            );
        }
    }
}
