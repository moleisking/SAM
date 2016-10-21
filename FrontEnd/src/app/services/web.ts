import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Observable } from "rxjs/Rx";

import { Settings } from "../config/settings";

@Injectable()
export class WebService {

  constructor(private http: Http) { }

  about(): Observable<string> {
    return this.http.get(Settings.backend_url + "/about")
      .map((res: Response) => res.json().about).catch(this.handleError);
  }

  termsConditions(): Observable<string> {
    return this.http.get(Settings.backend_url + "/termsconditions")
      .map((res: Response) => res.json().termsconditions).catch(this.handleError);
  }

  cookiePolicy(): Observable<string> {
    return this.http.get(Settings.backend_url + "/cookiepolicy")
      .map((res: Response) => res.json().cookiepolicy).catch(this.handleError);
  }

  sendContactForm(form: any): Observable<any> {
    let creds = "name=" + form.name + "&surname=" + form.surname + "&email=" + form.email
      + "&message=" + form.notification;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");

    return this.http.post(Settings.backend_url + "/sendcontactform", creds, { headers: headers })
      .map(this.extractData).catch(this.handleError);
  }

  private extractData(res: Response) {
    console.log(res);
    let body = res.json();
    return body.data || {};
  }

  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
