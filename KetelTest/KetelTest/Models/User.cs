using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTestWeb.Models
{
    public class User
    {
        public int ID { get; set; }
        public string Nickname { get; set; }
        public string PasswordHash { get; set; }
        public bool Sex { get; set; }
        public int Birthday { get; set; }

        public List<Statisics> Statisics { get; set; }
    }
}
