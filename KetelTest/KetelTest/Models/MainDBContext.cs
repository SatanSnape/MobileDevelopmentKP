using KetelTest.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace KetelTestWeb.Models
{
    public class MainDBContext : DbContext
    {

        public DbSet<User> Users { get; set; }
        public DbSet<Question> Questions { get; set; }
        public DbSet<Answer> Answers { get; set; }
        public DbSet<UserAnswer> UserAnswers { get; set; }
        public DbSet<Statisics> Statisics { get; set; }
        public DbSet<TestResult> TestResults { get; set; }
        public DbSet<TestFinal> TestFinals { get; set; }
        public DbSet<TestResultUser> TestResultUsers { get; set; }
        public DbSet<TestInterpretation> TestInterpretations { get; set; }
        public DbSet<FinalQuery> FinalQueries { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            base.OnConfiguring(optionsBuilder);
            optionsBuilder.UseSqlServer(@"Data Source=SAMSUNG\SQLEXPRESS02;Initial Catalog=KetelTest;Integrated Security=True");
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
            modelBuilder.Entity<Statisics>()
                .HasOne(x => x.User)
                .WithMany(x => x.Statisics)
                .HasForeignKey(x => x.UserID);

            modelBuilder.Entity<Statisics>()
                .HasOne(x => x.TestResult)
                .WithMany(x => x.Statisics)
                .HasForeignKey(x => x.TestResultID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.TestResult)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.TestResultID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.Question)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.QuestionID);

            modelBuilder.Entity<UserAnswer>()
                .HasOne(x => x.Answer)
                .WithMany(x => x.UserAnswers)
                .HasForeignKey(x => x.AnswerID);

            modelBuilder.Entity<Answer>()
                .HasOne(x => x.Question)
                .WithMany(x => x.Answers)
                .HasForeignKey(x => x.QuestionID);

            modelBuilder.Entity<TestFinal>()
                .HasOne(x => x.Answer)
                .WithMany(x => x.TestFinals)
                .HasForeignKey(x => x.AnswerID);

            modelBuilder.Entity<TestFinal>()
                .HasOne(x => x.Question)
                .WithMany(x => x.TestFinals)
                .HasForeignKey(x => x.QuestionID);

            modelBuilder.Entity<TestResultUser>()
                .HasOne(x => x.TestFinal)
                .WithMany(x => x.TestResultUsers)
                .HasForeignKey(x => x.TestFinalID);

            modelBuilder.Entity<TestResultUser>()
                .HasOne(x => x.TestResult)
                .WithMany(x => x.TestResultUsers)
                .HasForeignKey(x => x.TestResultID);

            modelBuilder.Entity<FinalQuery>()
                .HasOne(x => x.TestInterpretation)
                .WithMany(x => x.FinalQueries)
                .HasForeignKey(x => x.TestPresentation);
        }
    }
}
