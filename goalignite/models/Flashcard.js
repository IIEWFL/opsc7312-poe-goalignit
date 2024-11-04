//Code adapted from Youtube
//https://www.bing.com/videos/riverview/relatedvideo?q=api+endpoints+node.js+and+vs+code&mid=569679533D1F5343BA2F569679533D1F5343BA2F&FORM=VIRE
//Wise Fun-How to run Node js with VS Code | Install Node js | npm | VS Code | Setup Node js(2020)

const mongoose = require('mongoose');

const flashcardSchema = new mongoose.Schema({
    module: String,
    question: String,
    answer: String,
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Flashcard', flashcardSchema);
