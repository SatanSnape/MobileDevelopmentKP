using KetelTest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTestWeb.Models
{
    public class TestResult
    {
        public int ID { get; set; }

        public DateTime DateTime { get; set; }

        public List<UserAnswer> UserAnswers { get; set; }
        public List<Statisics> Statisics { get; set; }
        public List<TestResultUser> TestResultUsers { get; set; }
    }
}
