
const Data = {
  geojson: async() => d3.json("./source/map/municipalities.geojson"),
  municip: async() => d3.csv("./source/map/municipalities.csv"), 
  summary: async() => {
    const years = [2018, 2019, 2020, 2021, 2022, 2023, 2024];
      const exam = async(year) => {
        const DATA_URLS = {
        2018: "./source/exam/nvo/grade-07/by_school/2018-2019.json",
        2019: "./source/exam/nvo/grade-07/by_school/2019-2020.json",
        2020: "./source/exam/nvo/grade-07/by_school/2020-2021.json",
        2021: "./source/exam/nvo/grade-07/by_school/2021-2022.json",
        2022: "./source/exam/nvo/grade-07/by_school/2022-2023.json",
        2023: "./source/exam/nvo/grade-07/by_school/2023-2024.json",
        2024: "./source/exam/nvo/grade-07/by_school/2024-2025.json"
      };
      const url = DATA_URLS[year];
      if (!url) throw new Error(`No data for year ${year}`);
      const response = await fetch(url);
      return await response.json();
    };
    const summary = {};
    for (const year of years) {
      const data = await exam(year);
      summary[year] = Array.isArray(data) ? data : [];
    }
    return summary;
  }
};