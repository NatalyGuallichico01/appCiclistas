import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthserviceService } from 'src/app/services/authservice.service';

@Component({
  selector: 'app-forgotpassword',
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css']
})
export class ForgotpasswordComponent implements OnInit {
//Se genera la variable del correo
email:string= '';
constructor(private auth : AuthserviceService, public router: Router) { }

  ngOnInit(): void {
    localStorage.setItem("recarga", "true");
  }
//Método para reestablecer contraseña
  forgotPassword(){
    
//Referencia al método del servicio para reestablecer contraseña
    this.auth.forgotPassword(this.email);
    this.myAsyncFunction();
//reestablecer correo
    this.email= '';
  }
  async delay(n: number){
    return new Promise(function(resolve){
        setTimeout(resolve,n*1000);
    });
  }
  async  myAsyncFunction(){
    await this.delay(2);
    this.router.navigate(['/login']);
  }
  
  

}
