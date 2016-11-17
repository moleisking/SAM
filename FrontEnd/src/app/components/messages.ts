import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";
import { Observable, Subscription } from "rxjs/Rx";

import { UserService } from "../services/user";
import { MessageService } from "../services/message";
import { AuthService } from "../services/auth";
import { TranslateService } from "ng2-translate";

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
    private url: string;
    private email: string;
    private messages: MessageModel[];

    constructor(
        private route: ActivatedRoute,
        private user: UserService,
        private sanitizer: DomSanitizer,
        private m: MessageService,
        private authService: AuthService,
        private trans: TranslateService
    ) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(p => {
            let id = p["id"];
            this.getProfile$ = this.user.getProfile(id).subscribe(
                profile => {
                    this.name = profile.name;
                    this.url = profile.url;
                    this.email = profile.email;
                    this.image = profile.image === "" ? this.defaultImage : profile.image;
                    this.trans.get("LoadingMessages").subscribe((res: string) => this.message = res);
                    this.messages$ = this.m.readWith(profile.url).subscribe(
                        messages => {
                            messages.forEach(element => {
                                if (element.from === profile.email)
                                    element.from = profile.name;
                                else
                                    this.trans.get("Me").subscribe((res: string) => element.from = res);
                            });
                            this.messages = messages;
                            this.message = "";
                        },
                        error => this.message = <any>error,
                        () => this.trans.get("DoneGetMessages").subscribe((res: string) => console.log(res))
                    );
                },
                error => this.message = <any>error,
                () => this.trans.get("DoneGetProfile").subscribe((res: string) => console.log(res))
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
        this.getProfile$.unsubscribe();
        this.messages$.unsubscribe();
    }

    sendMessage(messageText: string) {
        if (!messageText || !this.email || this.email === "undefined" || !this.url || this.url === "undefined")
            this.trans.get("MessageNotValid").subscribe((res: string) => this.message = res);
        else if (messageText.length < 4 || messageText.length > 500)
            this.trans.get("MessageNoCharacters").subscribe((res: string) => this.message = res);
        else {
            this.trans.get("MessageProfileSent").subscribe((res: string) => this.message = res);

            let model = new MessageModel();
            model.to = this.email;
            model.url = this.url;
            model.text = messageText;

            this.m.add(model).subscribe(
                m => {
                    this.trans.get("MessageSent").subscribe((res: string) => this.message = res);
                    this.trans.get("Me").subscribe((res: string) => m.from = res);
                    this.messages.push(m);
                    // clean the new message form
                    this.message = "";
                    this.messageText.nativeElement.value = "";
                },
                res => this.message = res,
                () => {
                    jQuery("#SendMessageModal").modal("hide");
                    this.trans.get("DoneSendMessage").subscribe((res: string) => console.log(res));
                }
            );
        }
    }
}