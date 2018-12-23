using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTest.Models.NetInputModels
{
    public class UserSave
    {
        public int ID { get; set; }
        public string Nickname { get; set; }
        public bool Sex { get; set; }
        public int Age { get; set; }
    }
}
