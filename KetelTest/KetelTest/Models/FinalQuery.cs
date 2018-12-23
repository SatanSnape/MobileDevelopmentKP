using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTest.Models
{
    public class FinalQuery
    {
        public int ID { get; set; }

        public int Ticket { get; set; }

        public int TestPresentation { get; set; }
        public string Result { get; set; }
        public TestInterpretation TestInterpretation { get; set; }
    }
}
