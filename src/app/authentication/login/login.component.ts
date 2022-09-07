import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthserviceService } from 'src/app/services/authservice.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  //Variables para el correo y contraseña
  email: string='';
  password: string= '';
  public userForm: FormGroup;
  public user: any;
  
  constructor(private auth: AuthserviceService, 
    public formBuilder: FormBuilder, 
    private router : Router,) {
      this.userForm= this.formBuilder.group({
        
        mail: [''],
        password: [''],
        id: null
      })
     }

  ngOnInit(): void {
    //Metodo para borrar el almacenamiento interno
    localStorage.clear();
    localStorage.setItem("recarga", "true");
  }
  //Referencia al método para Iniciar sesión y verificación de algun error
  async login(){
    const res = await this.auth.login(this.userForm.value).catch(error =>{
      alert("Credenciales Incorrectas")
      console.log('error', error);
    })
   //Respuestas
    if(res){
        const path = "users";
        //Asignación del id
        const id:string= res.user.uid;
        console.log("id user: ", id)
        //creario usuario en Firestore
        await this.auth.getObject(id, path).subscribe( res =>{
          this.user = res;
         localStorage.setItem("idUser", this.user.id);
         console.log("Datos usuario: ",
         localStorage.getItem("idUser"),
         )
        })
        if(res.user?.emailVerified == true) {
          this.router.navigate(['dashboard']);
        } else {
          this.router.navigate(['/verify']);
        }
      }
  }
  emptyFields(field: string){
    if (this.userForm.get(field)?.hasError('required')) {
      return 'El campo es obligatorio';
    }
   
    return this.userForm.get(field)? 'Formato incorrecto' : '';
  }
  
  get emptyPassword(){
    return this.userForm.get('password')?.invalid && this.userForm.get('password')?.touched
  }
  get emptyemail(){
    return this.userForm.get('mail')?.invalid && this.userForm.get('mail')?.touched
  }
  

}
