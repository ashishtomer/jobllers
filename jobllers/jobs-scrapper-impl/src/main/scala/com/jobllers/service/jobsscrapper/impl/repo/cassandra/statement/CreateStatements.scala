package com.jobllers.service.jobsscrapper.impl.repo.cassandra.statement

object CreateStatements {

  val createJobScrapperTable =
    """CREATE TABLE IF NOT EXISTS scrapped_job_by_date_company_rolecategory_id (
      |job_id ascii,
      |job_url ascii,
      |job_title ascii,
      |company ascii,
      |experience ascii ,
      |job_locations list<ascii>,
      |posted_by ascii,
      |popularity_tag ascii,
      |job_views int,
      |job_applicants int,
      |job_date timeuuid,
      |industry list<ascii>,
      |functional_area list<ascii>,
      |role_category ascii,
      |role ascii,
      |employement_type ascii,
      |keyskills list<ascii>,
      |job_description ascii,
      |banner_on_listing boolean,
      |company_profile ascii,
      |desired_candidate_profile ascii,
      |primary key (job_date, company, role_category, job_id)
      |);
    """.stripMargin
}
