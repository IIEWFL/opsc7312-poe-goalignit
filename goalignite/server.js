//Code adapted from Youtube
//https://www.bing.com/videos/riverview/relatedvideo?q=api+endpoints+node.js+and+vs+code&mid=569679533D1F5343BA2F569679533D1F5343BA2F&FORM=VIRE
//Wise Fun-How to run Node js with VS Code | Install Node js | npm | VS Code | Setup Node js(2020)

const express = require('express');
const mongoose = require('mongoose');
const multer = require('multer');
const cors = require('cors');
const bodyParser = require('body-parser');
const app = express();
const port = 3004;
const Flashcard = require('./models/Flashcard.js');
const router = express.Router();

// Connect to MongoDB
mongoose.connect('mongodb://localhost:27017/mediaDB')
    .then(() => console.log('MongoDB connected')) // Log success message
    .catch(err => console.error('MongoDB connection error:', err));

    // Configure multer for file storage
const storage = multer.diskStorage({
    // Define where to store uploaded files
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + Path2D.extname(file.originalname));
    }
});
const upload = multer({ storage: storage });// Create multer instance with defined storage

// Middleware
app.use(cors());
// Parse JSON request bodies
app.use(express.json()); // Middleware to parse JSON
app.use('/uploads', express.static('uploads'));

// Add flashcard file
app.post('/api/flashcards', async (req, res) => { // Create a new flashcard instance from request body
    const flashcard = new Flashcard(req.body);
    await flashcard.save();
    res.status(201).send(flashcard);
});

// Read all flashcards
app.get('/api/flashcards', async (req, res) => {
    try {
        const flashcards = await Flashcard.find(); //Retrieves issues
        res.send(flashcards);
    } catch (error) {
        res.status(500).send({ message: 'Error loading flashcards' });
    }
});
// Read all flashcards using ID
app.get('/api/flashcards/:id', async (req, res) => {
    const flashcards = await Flashcard.find();
    res.send(flashcards);
});

// Update using ID
app.put('/api/flashcards/:id', async (req, res) => { // Retrieve flashcard by ID
    const flashcard = await Flashcard.findByIdAndUpdate(req.params.id, req.body, { new: true });
    res.send(flashcard);
});

// Delete all flashcards
app.delete('/api/flashcards', async (req, res) => { 
    await Flashcard.deleteMany();
    res.status(204).send();
});
//deletes flashcard using id
app.delete('/api/flashcards/:id', async (req, res) => {
    await Flashcard.findByIdAndDelete(req.params.id);
    res.sendStatus(204);
});

// Middleware to handle requests to the /api route
app.use('/api', (req, res, next) => {
    next(); // Proceed to the next middleware or route handler
});

// Start the server and listen on the defined port
app.listen(port, () => {
    console.log(`Server is running on port ${port}`); // Log server start message
});