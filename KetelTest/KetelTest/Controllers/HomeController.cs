using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using KetelTest.Models.NetInputModels;
using KetelTest.Models.NetModels;
using KetelTest.Services;
using KetelTestWeb.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace KetelTest.Controllers
{
    public static class Extensions
    {
        public static bool Compare<T>(this List<T> l1, List<T> l2)
        {
            foreach (T el in l1)
                if (!l2.Contains(el))
                    return false;
            return true;
        }
    }



    public class HomeController : Controller
    {
        /// <summary>
        /// Получить в представлении IActionResultNet вопрос с ответами
        /// </summary>
        /// <param name="question"></param>
        /// <param name="mainDBContext"></param>
        /// <returns></returns>
        private QuestionActionNet GetQuestionActionNet(Question question, MainDBContext mainDBContext, List<UserAnswer> questions)
        {
            var userAnswer = questions.FirstOrDefault(x => x.QuestionID == question.ID);

            return new QuestionActionNet
            {
                WorkID = question.ID,
                ResultCode = userAnswer == null ? ResultCode.Error : ResultCode.Success,
                QuestionContent = question.QuestionContent,
                AnswerActionNets = mainDBContext.Answers.Where(y => y.Question == question)
                    .Select(y => new AnswerActionNet
                    {
                        ResultCode = userAnswer != null && userAnswer.AnswerID == y.ID ? ResultCode.Success : ResultCode.Error,
                        WorkID = y.ID,
                        AnswerContent = y.AnswerContent
                    }).ToList()
            };
        }

        /// <summary>
        /// Войти в систему
        /// </summary>
        /// <param name="userModel"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Login([FromBody]UserModel userModel)
        {
            if (!ModelState.IsValid)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                {
                    ResultCode = ResultCode.Error,
                    Message = string.Join("\n", ModelState.Values.Select(x => x.Errors).SelectMany(x => x).Select(x => x.ErrorMessage))
                }));
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.FirstOrDefault(x => x.Nickname == userModel.Login);
            if (user != null)
                if (CryptService.GetMd5Hash(userModel.Password) == user.PasswordHash)
                    return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                    {
                        ResultCode = ResultCode.Success,
                        WorkID = user.ID,
                        IsTestWork = mainDBContext.Statisics.Where(x => x.User == user).Count() > 0
                    }));
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword }));
        }

        /// <summary>
        /// Получить статистики
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject GetStatistics([FromBody]IntegerInput user)
        {
            MainDBContext mainDBContext = new MainDBContext();
            List<StatisticResult> statisticResults = new List<StatisticResult>();
            var testResults = mainDBContext.Statisics.Where(x => x.UserID == user.Value).Select(x => x.TestResult).ToList();
            var questionsCount = mainDBContext.Questions.Count();
            var groupResults = mainDBContext.FinalQueries.GroupBy(x => x.Ticket);
            var testInterpretations = mainDBContext.TestInterpretations.GroupBy(x => x.Type);
            foreach (var testResult in testResults)
            {
                var usersAnswer = mainDBContext.TestResultUsers.Where(x => x.TestResult == testResult)
                    .Select(x => x.TestFinal).ToList();
                if (usersAnswer.Count > 0)
                {
                    var userGroups = usersAnswer.GroupBy(x => x.Type).ToDictionary(x => x.Key, x => x.Sum(y => y.Mark));
                    foreach (var testInter in testInterpretations)
                        if (!userGroups.TryGetValue(testInter.Key, out var val))
                            userGroups.Add(testInter.Key, 0);
                    var testPresent = mainDBContext.TestInterpretations.Where(x => userGroups.Keys.Contains(x.Type)
                            && userGroups[x.Type] > x.MarkMin && userGroups[x.Type] <= x.MarkMax).ToList();
                    string res = string.Empty;
                    foreach (var group in groupResults)
                        if (group.Select(x => x.TestPresentation).ToList().Compare(testPresent.Select(x => x.ID).ToList()))
                            res += group.FirstOrDefault().Result;
                    if (!string.IsNullOrEmpty(res))
                    {
                        statisticResults.Add(new StatisticResult
                        {
                            DateTime = testResult.DateTime.ToShortDateString(),
                            Result = res,
                            ResultCode = ResultCode.Success,
                            WorkID = testResult.ID
                        });
                    }
                }
            }


            return JObject.Parse(JsonConvert.SerializeObject(new StatisticsList
            {
                ResultCode = statisticResults.Count() > 0 ? ResultCode.Success : ResultCode.Error,
                WorkID = user.Value,
                staticticResults = statisticResults
            }));
        }

        /// <summary>
        /// Регистрация
        /// </summary>
        /// <param name="registerModel"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Register([FromBody]RegisterModel registerModel)
        {
            if (!ModelState.IsValid)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
                {
                    ResultCode = ResultCode.Error,
                    Message = string.Join("\n", ModelState.Values.Select(x => x.Errors).SelectMany(x => x).Select(x => x.ErrorMessage))
                }));
            if (registerModel.Password != registerModel.PasswordConfirmed)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword }));
            MainDBContext mainDBContext = new MainDBContext();
            if (mainDBContext.Users.FirstOrDefault(x => x.Nickname == registerModel.Login) != null)
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.ErrorLoginPassword, WorkID = 0 }));
            var user = new User
            {
                Nickname = registerModel.Login,
                PasswordHash = CryptService.GetMd5Hash(registerModel.Password),
                Birthday = registerModel.Birthday,
                Sex = registerModel.Sex
            };
            mainDBContext.Entry(user).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.Success, WorkID = user.ID }));
        }

        /// <summary>
        /// Начать тест
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject StartTest([FromBody]IntegerInput user)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var countQuestions = mainDBContext.Questions.Count();
            var prevTest = mainDBContext.Statisics.Where(x => x.UserID == user.Value)
                .Select(x => x.TestResult).FirstOrDefault(x => x.UserAnswers.Count() != countQuestions);
            if (prevTest != null)
                return JObject.Parse(JsonConvert.SerializeObject(new StartActionNet { IsStartPrev = true, PrevID = prevTest.ID, ResultCode = ResultCode.Success, WorkID = user.Value }));
            var testResult = new TestResult { DateTime = DateTime.Now };
            var statistics = new Statisics { TestResult = testResult, UserID = user.Value };
            mainDBContext.Entry(testResult).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.Entry(statistics).State = Microsoft.EntityFrameworkCore.EntityState.Added;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new StartActionNet { IsStartPrev = false, WorkID = user.Value, ResultCode = ResultCode.Success, PrevID = testResult.ID }));
        }

        /// <summary>
        /// Завершить тест
        /// </summary>
        /// <param name="integer"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject FinishTest([FromBody]FinishTestInput integer)
        {
            MainDBContext mainDBContext = new MainDBContext();
            if (integer.Timeout)
            {
                var test = mainDBContext.TestResults.Find(integer.Value);
                mainDBContext.Statisics.Remove(mainDBContext.Statisics.FirstOrDefault(x => x.TestResult == test));
                mainDBContext.TestResults.Remove(test);
                mainDBContext.SaveChanges();
                return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.Success }));
            }
            int count = mainDBContext.UserAnswers.Where(x => x.TestResultID == integer.Value).Count();
            int qcount = mainDBContext.Questions.Count();
            if (qcount == count)
            {
                var tr = mainDBContext.TestResults.Find(integer.Value);
                foreach (var userAnswer in mainDBContext.UserAnswers.Where(x => x.TestResult == tr))
                {
                    var answer = mainDBContext.TestFinals.FirstOrDefault(x => x.QuestionID == userAnswer.QuestionID && x.AnswerID == userAnswer.AnswerID);
                    if (answer != null)
                    {
                        mainDBContext.TestResultUsers.Add(new Models.TestResultUser
                        {
                            TestFinal = answer,
                            TestResultID = integer.Value
                        });
                    }
                }
                mainDBContext.SaveChanges();
            }
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = count == qcount ? ResultCode.Success : ResultCode.Error,
                WorkID = integer.Value
            }));
        }

        /// <summary>
        /// Получить вопросы
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject GetQuestions([FromBody]IntegerInput id)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var questions = mainDBContext.UserAnswers.Where(x => x.TestResultID == id.Value).ToList();
            var quesionsList = mainDBContext.Questions.ToList();

            return JObject.Parse(JsonConvert.SerializeObject(new QuestionListActionNet
            {
                ResultCode = quesionsList.Count > 0 ? ResultCode.Error : ResultCode.Success,
                WorkID = id.Value,
                QuestionActionNets = quesionsList.Select(x => GetQuestionActionNet(x, mainDBContext, questions)).ToList()
            }));
        }

        /// <summary>
        /// Ответить на вопрос
        /// </summary>
        /// <param name="userAnswer"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject Answer([FromBody] UserAnswerInputNet userAnswer)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var isAnswered = mainDBContext.UserAnswers
                .FirstOrDefault(x => x.QuestionID == userAnswer.QuestionID
                && x.TestResultID == userAnswer.TestResult);

            if (isAnswered == null)
                mainDBContext.UserAnswers.Add(new UserAnswer
                {
                    AnswerID = userAnswer.AnswerID,
                    QuestionID = userAnswer.QuestionID,
                    TestResultID = userAnswer.TestResult
                });

            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = ResultCode.Right,
                WorkID = userAnswer.UserID
            }));
        }

        /// <summary>
        /// Пользовательский ответ на предложение продолжить тест
        /// </summary>
        /// <param name="isStartTest"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject IsStartTest([FromBody]IsStartTest isStartTest)
        {
            MainDBContext mainDBContext = new MainDBContext();
            if (!isStartTest.IsStart)
            {
                var test = mainDBContext.TestResults.Find(isStartTest.TestID);
                mainDBContext.Statisics.Remove(mainDBContext.Statisics.FirstOrDefault(x => x.TestResult == test));
                mainDBContext.TestResults.Remove(test);
                mainDBContext.SaveChanges();
            }
            int resID = 0;
            if (isStartTest.IsStart)
                resID = isStartTest.TestID;
            else
                resID = JsonConvert.DeserializeObject<StartActionNet>(StartTest(
                    new IntegerInput
                    {
                        Value = isStartTest.UserID
                    }).ToString()).PrevID;
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet
            {
                ResultCode = isStartTest.IsStart ? ResultCode.Success : ResultCode.Error,
                WorkID = resID
            }));
        }


        /// <summary>
        /// Получить пользовательский профиль
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [HttpPost]
        public JObject ProfileUserInfo([FromBody] IntegerInput id)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.Find(id.Value);
            return JObject.Parse(JsonConvert.SerializeObject(new UserProfileNet
            {
                Birthday = user.Birthday,
                Nickname = user.Nickname,
                ResultCode = ResultCode.Success,
                Sex = user.Sex,
                WorkID = user.ID
            }));
        }

        [HttpPost]
        public JObject SaveUser([FromBody] UserSave userSave)
        {
            MainDBContext mainDBContext = new MainDBContext();
            var user = mainDBContext.Users.Find(userSave.ID);
            user.Birthday = userSave.Age;
            user.Nickname = userSave.Nickname;
            user.Sex = userSave.Sex;
            mainDBContext.SaveChanges();
            return JObject.Parse(JsonConvert.SerializeObject(new UniversalActionNet { ResultCode = ResultCode.Success }));
        }

    }
}
