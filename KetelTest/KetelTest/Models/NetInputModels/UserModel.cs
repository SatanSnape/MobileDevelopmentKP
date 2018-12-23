﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTest.Models.NetInputModels
{
    public class UserModel
    {
        [Required(ErrorMessage = "Login is required")]
        [DataType(DataType.EmailAddress)]
        [EmailAddress]
        public string Login { get; set; }
        [Required(ErrorMessage = "Password is required")]
        [DataType(DataType.Password)]
        public string Password { get; set; }
    }

    public class RegisterModel
    {
        [Required(ErrorMessage = "Email is required")]
        [DataType(DataType.EmailAddress)]
        [EmailAddress]
        public string Login { get; set; }
        [Required(ErrorMessage = "Password is required")]
        [DataType(DataType.Password)]
        public string Password { get; set; }
        [Required(ErrorMessage = "Confirmed password is required")]
        [DataType(DataType.Password)]
        public string PasswordConfirmed { get; set; }
        [Required(ErrorMessage = "Birthday is required")]
        public int Birthday { get; set; }
        [Required(ErrorMessage = "Sex is required")]
        public bool Sex { get; set; }
    }
}
