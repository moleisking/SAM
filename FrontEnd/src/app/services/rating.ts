import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { TranslateService } from "ng2-translate";

import { Settings } from "../config/settings";
import { RatingModel } from "../models/rating";

@Injectable()
export class RatingService {

    constructor(
        private http: Http,
        private trans: TranslateService
    ) { }

    add(model: RatingModel): Observable<any> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        headers.append("Content-Type", "application/x-www-form-urlencoded");
        let options = new RequestOptions({ headers: headers });
        let body = "id=" + model.id + "&number=" + model.number;

        return this.http.post(Settings.backend_url + "/ratings/add?locale=" + this.trans.currentLang, body, options)
            .map((res: Response) => res.json().add).catch(this.handleError);
    }

    readProfileAuth(nameUrl: string): Observable<RatingModel> {
        let headers = new Headers();
        headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
        let options = new RequestOptions({ headers: headers });

        return this.http.get(Settings.backend_url + "/ratings/readProfileAuth/" + nameUrl
            + "?locale=" + this.trans.currentLang, options)
            .map((res: Response) => res.json()).catch(this.handleError);
    }

    readProfile(nameUrl: string): Observable<RatingModel> {
        return this.http.get(Settings.backend_url + "/ratings/readprofile/" + nameUrl
            + "?locale=" + this.trans.currentLang).map((res: Response) => res.json()).catch(this.handleError);
    }

    private handleError(error: any) {
        // console.error(error);
        let errMsg = (error.message) ? error.message :
            (error._body) ? error._body : error.status ? `${error.status} - ${error.statusText}` : "Server error";
        console.error(errMsg);
        return Observable.throw(errMsg);
    }
}
