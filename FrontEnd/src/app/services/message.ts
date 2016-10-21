import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { Settings } from "../config/settings";
import { MessageModel } from "../models/message";

@Injectable()
export class MessageService {

    constructor(private http: Http) { }

    add(model: MessageModel): Observable<any> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        headers.append("Content-Type", "application/x-www-form-urlencoded");
        let options = new RequestOptions({ headers: headers });
        let body = "to=" + model.to + "&text=" + model.text + "&front=" + Settings.frontend_url
            + "&fromUrl=" + model.nameurl;

        return this.http.post(Settings.backend_url + "/messages/add", body, options)
            .map((res: Response) => res.json().message).catch(this.handleError);
    }

    readAllLasts(): Observable<MessageModel[]> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        let options = new RequestOptions({ headers: headers });

        return this.http.get(Settings.backend_url + "/messages/readalllasts", options)
            .map((res: Response) => res.json().messages).catch(this.handleError);
    }

    readWith(name: string): Observable<MessageModel[]> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        let options = new RequestOptions({ headers: headers });

        return Observable.interval(5000)
            .flatMap(() => this.http.get(Settings.backend_url + "/messages/read/" + name, options)
                .map((res: Response) => res.json().messages).catch(this.handleError));
    }

    private handleError(error: any) {
        // console.error(error);
        let errMsg = (error.message) ? error.message :
            (error._body) ? error._body : error.status ? `${error.status} - ${error.statusText}` : "Server error";
        console.error(errMsg);
        return Observable.throw(errMsg);
    }
}
