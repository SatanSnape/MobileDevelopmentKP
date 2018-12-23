using KetelTestWeb.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTest.Models
{
    public class TestResultUser
    {
        public int ID { get; set; }
        public TestFinal TestFinal { get; set; }
        public int TestFinalID { get; set; }

        public TestResult TestResult { get; set; }
        public int TestResultID { get; set; }
    }

    public class TestFinal
    {
        public int ID { get; set; }

        public Question Question { get; set; }
        public int QuestionID { get; set; }

        public Answer Answer { get; set; }
        public int AnswerID { get; set; }

        public string Type { get; set; }
        public int Mark { get; set; }

        public List<TestResultUser> TestResultUsers { get; set; }
    }

    public class TestInterpretation
    {
        public int ID { get; set; }

        public string Type { get; set; }
        public int MarkMin { get; set; }
        public int MarkMax { get; set; }

        public List<FinalQuery> FinalQueries { get; set; }
    }
}
