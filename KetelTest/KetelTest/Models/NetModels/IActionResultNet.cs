﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTest.Models.NetModels
{
    public enum ResultCode
    {
        Success,
        Error,
        ErrorLoginPassword,
        Wrong,
        Right
    }

    public enum Boolean
    {
        True,
        False
    }

    public interface IActionResultNet
    {
        ResultCode ResultCode { get; set; }
        int WorkID { get; set; }
    }

    public class UniversalActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string Message { get; set; }
        public bool IsTestWork { get; set; }
    }

    public class StartActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public bool IsStartPrev { get; set; }
        public int PrevID { get; set; }
    }

    public class AnswerActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string AnswerContent { get; set; }
    }

    public class QuestionActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string QuestionContent { get; set; }
        public List<AnswerActionNet> AnswerActionNets { get; set; }
    }

    public class QuestionListActionNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public List<QuestionActionNet> QuestionActionNets { get; set; }
    }

    public class StatisticResult : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string Result { get; set; }
        public string DateTime { get; set; }
    }

    public class StatisticsList : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public List<StatisticResult> staticticResults { get; set; }
    }

    public class UserProfileNet : IActionResultNet
    {
        public ResultCode ResultCode { get; set; }
        public int WorkID { get; set; }
        public string Nickname { get; set; }
        public bool Sex { get; set; }
        public int Birthday { get; set; }
    }
}
